#![no_main]
#![no_std]

use alloc::boxed::Box;
use board::{BoardMxAz3166, LowLevelInit};

use defmt::println;
use static_cell::StaticCell;
use threadx_rs::allocator::ThreadXAllocator;

use threadx_rs::WaitOption;
use threadx_rs::pool::BytePool;
use threadx_rs::queue::Queue;
use threadx_rs::thread::{Thread, sleep};

extern crate alloc;
#[derive(Clone, Copy)]
pub enum Event {
    Event,
    Info(u32),
}
#[global_allocator]
static GLOBAL: ThreadXAllocator = ThreadXAllocator::new();

static HEAP: StaticCell<[u8; 1024]> = StaticCell::new();
static BP_MEM: StaticCell<[u8; 1024]> = StaticCell::new();
static QUEUE: StaticCell<Queue<Event>> = StaticCell::new();
static THREAD1: StaticCell<Thread> = StaticCell::new();
static THREAD2: StaticCell<Thread> = StaticCell::new();
static BP: StaticCell<BytePool> = StaticCell::new();

#[cortex_m_rt::entry]
fn main() -> ! {
    defmt::println!("Hello, world!");

    let tx = threadx_rs::Builder::new(
        // low level initialization
        |ticks_per_second| {
            BoardMxAz3166::low_level_init(ticks_per_second);
        },
        // Start of Application definition
        |mem_start| {
            defmt::println!("Define application. Memory starts at: {} ", mem_start);
            let heap = HEAP.init([0u8; 1024]);
            GLOBAL.initialize(heap).unwrap();
            let bp_mem = BP_MEM.init([0u8; 1024]);
            let bp = BP.init(BytePool::new());

            let bp = bp.initialize(c"pool1", bp_mem).unwrap();
            //allocate memory for the two tasks.
            let task1_mem = bp.allocate(256, true).unwrap();
            let task2_mem = bp.allocate(256, true).unwrap();
            let queue_mem = bp.allocate(64, true).unwrap();
            let queue = QUEUE.init(Queue::new());
            let (sender, receiver) = queue.initialize(c"queue", queue_mem.consume()).unwrap();

            let thread = THREAD1.init(Thread::new());
            let thread1_func = move || {
                let arg: u32 = 0;

                println!("Thread 1:{}", arg);
                let mut count: u32 = 1;
                loop {
                    let message = Event::Info(count);
                    sender.send(message, WaitOption::WaitForever).unwrap();
                    count += 1;
                    sleep(core::time::Duration::from_millis(1000)).unwrap();
                }
            };

            let _th_handle = thread
                .initialize_with_autostart_box(
                    c"thread1",
                    Box::new(thread1_func),
                    task1_mem.consume(),
                    1,
                    1,
                    0,
                )
                .unwrap();

            let thread2_fn = move || loop {
                let msg = receiver.receive(WaitOption::WaitForever).unwrap();
                match msg {
                    Event::Event => {
                        println!("Thread 2: RX Event");
                    }
                    Event::Info(info) => {
                        println!("Thread 2: RX Info:{}", info);
                    }
                }
            };
            let thread2 = THREAD2.init(Thread::new());

            let _th2_handle = thread2
                .initialize_with_autostart_box(
                    c"thread2",
                    Box::new(thread2_fn),
                    task2_mem.consume(),
                    1,
                    1,
                    0,
                )
                .unwrap();
            println!("Init done.");
        },
    );

    tx.initialize();
    println!("Exit");
    threadx_app::exit()
}
