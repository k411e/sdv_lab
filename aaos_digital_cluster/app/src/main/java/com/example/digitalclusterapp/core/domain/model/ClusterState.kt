package com.example.digitalclusterapp.core.domain.model

/**
 * Represents the state of the vehicle's instrument cluster display.
 *
 * @property speed Current vehicle speed in mph
 * @property rpm Current engine RPM (x1000)
 * @property speedUnit Current vehicle speed unit
 * @property modeTop Top driving mode display text
 * @property modeMid Middle driving mode display text (current mode)
 * @property modeBottom Bottom driving mode display text
 * @property gear Current transmission gear (P, R, N, D)
 * @property ambientTempC Ambient temperature in Celsius
 * @property economy Current fuel economy
 */
data class ClusterState(
    val speed: Int = 0,
    val rpm: Int = 0,
    val speedUnit: String = "mph",
    val modeTop: String = "Race",
    val modeMid: String = "Sport+",
    val modeBottom: String = "City",
    val gear: Char = 'D',
    val ambientTempC: Int = 25,
    val economy: String = "11.6 km/L",
    val cruiseControl: Boolean = false,
    val location: Boolean = false,
    val currentCentralScreen: CentralScreenState = CentralScreenState.MODES,
    val typeOfVehicle: VehicleType = VehicleType.COMBUST
)