package io.btkit.btlib.model;

class RuuviTag() {

    @JvmField
    var dataFormat: Int? = null
    @JvmField
    var temperature: Double? = null
    @JvmField
    var humidity: Double? = null
    @JvmField
    var pressure: Double? = null
    @JvmField
    var accelX: Double? = null
    @JvmField
    var accelY: Double? = null
    @JvmField
    var accelZ: Double? = null
    @JvmField
    var voltage: Double? = null
    @JvmField
    var txPower: Double? = null
    @JvmField
    var movementCounter: Int? = null
    @JvmField
    var measurementSequenceNumber: Int? = null

}


