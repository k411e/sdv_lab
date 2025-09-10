
# Adding a New Feature to Digital Cluster App:

   **We will use the Cruise Control as the Tutorial**

This tutorial demonstrates how to add a new feature cruise control as an example. We'll cover adding the UI component, handling MQTT messages, and implementing the feature logic.

## Step 1: Update the Domain Model

First, add the cruise control property to the ClusterState data class, this way we have an easy accessible way to consult the values that are on the broker/state:

// In ::core.domain.model.ClusterState

data class ClusterState(
    // ...
    val cruiseControl: Boolean = false, // **Add the new Property**
    // ...
)

## Step 2: Add an Action for the Feature (optional)

This option is optional, because this is how we create an Action on our control board to toggle the cruise control. If your feature doesnt require an action from the use this should be skipped.

// In ::core.domain.action.ClusterAction

sealed class ClusterAction {
    //...
    object ToggleCruiseControl : ClusterAction() // **creating the new action for cruise control**
}

## Step 3: Update the Repository Interface (optional)

Add a method to toggle cruise control in the repository interface:

// In ::core.data.repository.MqttClusterRepository

interface MqttClusterRepository {    
    // ...
    suspend fun toggleCruiseControl(): Boolean // Add the method for cruise control
}

## Step 4: Implement the Repository Method (optional)

Implement the toggle method in the repository implementation:

// In ::core.data.repository.MqttClusterRepositoryImpl

class MqttClusterRepositoryImpl @Inject constructor(
    private val binder: MqttClusterBinder,
    private val publisher: MqttPublisher
) : MqttClusterRepository {
    
    // ...
    
    override suspend fun toggleCruiseControl(): Boolean {
        val currentState = state.value
        val newValue = !currentState.cruiseControl
        return publisher.publishKeyValue("cruisecontrol", newValue)
    }
}

## Step 5: Update the MQTT Message Handler

Ensure the MQTT message handler can process cruise control messages!

// In ::core.data.mqtt.MqttClusterBinder

private fun handleMessage(msg: UMessage) {
    // ...
    
    pairs.forEach { (key, rawValue) ->
        when (key.lowercase()) {
            // ...
            "cruisecontrol" -> rawValue.toBoolean().let { next = next.copy(cruiseControl = it) } // this will act like a filter where we filter the state of the cruise control on the broker
            // ...
        }
    }
    
    // ...
}

## Step 6: Add UI Assets

Add cruise control icons to the drawable resources:

ic_cruise_control_default.xml - Icon for inactive cruise control
ic_cruise_control_active.xml - Icon for active cruise control

## Step 7: Update the Top Bar to Display the Icon

Modify the ClusterTopBar component to show the cruise control icon:

// In ::feature.cluster.ui.component.ClusterTopBar

@Composable
fun ClusterTopBar(
    modifier: Modifier = Modifier,
    iconSpacing: Dp = 25.dp,
    cruiseControl: Boolean = false  // Add parameter
) {
    // ...
    
    val indicators: List<ClusterIndicator> = defaultIndicators(cruiseControl) // this can be improved by just sending the whole object of ClusterState and in the accordingly feature check what state it is in.
    
    // ...
}

@Composable
fun defaultIndicators(cruiseControl: Boolean): List<ClusterIndicator> {
    return listOf(
        // Other indicators...
        ClusterIndicator(
            drawableRes = if (cruiseControl) // will validate which state is currently on and define which drawable to populate
                R.drawable.ic_cruise_control_active 
            else 
                R.drawable.ic_cruise_control_default,
            visible = true
        ),
        // Other indicators...
    )
}

## Step 8: Add Control Button for Cruise Control (optional)

Add a button to toggle cruise control in the ControlButtons component:

// In ::feature.cluster.ui.component.ControlButtons

@Composable
fun ControlButtons(
    modifier: Modifier = Modifier,
    onAction: (ClusterAction) -> Unit,
    state: ClusterState  // Add state parameter
) {
    Row(
        // ...
    ) {
        // ...
        
        // Activate Cruise Control
        IconButton(onClick = { onAction(ClusterAction.ToggleCruiseControl) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    id = if (state.cruiseControl)
                        R.drawable.ic_cruise_control_active
                    else
                        R.drawable.ic_cruise_control_default
                ),
                contentDescription = "Toggle Cruise Control",
                tint = if (state.cruiseControl) Color.White else Color.Gray
            )
        }
        
        // ...
    }
}

## Step 9: Update the ViewModel to Handle the Action (optional)

Add a method in the ViewModel to handle the cruise control action:

// In ::feature.cluster.viewmodel.ClusterViewModel

class ClusterViewModel @Inject constructor(
    private val repository: MqttClusterRepository
) : ViewModel() {
    
    // Existing code...
    
    fun handleAction(action: ClusterAction) {
        when (action) {
            // Existing actions...
            is ClusterAction.ToggleCruiseControl -> toggleCruiseControl()
        }
    }
    
    // Add method to toggle cruise control
    private fun toggleCruiseControl() {
        viewModelScope.launch {
            repository.toggleCruiseControl()
        }
    }
}

## Step 10 Update the Cluster state object (no change needed hopefully)

This will be listening to the Mqtt Broker to validate if there is any change on the object/feature and will update accordingly.

fun updateClusterState(newState: ClusterState) {
    viewModelScope.launch {
        repository.updateClusterState(newState)
    }
}

## Step 11: Update the Main Cluster Component

Update the main Cluster component to pass the cruise control state:

// In ::feature.cluster.ui.screen.Cluster

@Composable
fun Cluster(
    modifier: Modifier = Modifier,
    state: ClusterState,
    onAction: (ClusterAction) -> Unit,
    // Other parameters...
) {
    Box(
        // ...
    ) {
        // ...
        
        Column(
            // ...
        ) {
            ClusterTopBar(
                modifier = Modifier.width(1100.dp),
                cruiseControl = state.cruiseControl  // Pass the state (**improvement** sending the whole object and validate if theres any change but can be too much to process so for now we are just sending what is required)
            )
        }
        
        // ...
    }
}

## Testing the Feature

We are using a mosquito server, for now, in a linux machine. For local testing purposes.
Go to the terminal in a Linux environment and do the following commands.

    sudo apt install -y mosquitto mosquitto-clients // Install the mosquitto server
    sudo systemctl enable --now mosquitto // enable the server

    mosquitto_pub -h 127.0.0.1 -p 1883 -V mqttv5 -t 'd/test/9999/01/dead/device/0001/01/1000' -m 'cruisecontrol=true' // <feature_mqtt_filter>=<value>

Verify the cruise control activates in the app

**This modular approach keeps the code organized and makes it easy to add new features without affecting existing functionality.**