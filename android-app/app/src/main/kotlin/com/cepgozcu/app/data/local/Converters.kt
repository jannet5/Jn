package com.cepgozcu.app.data.local

import androidx.room.TypeConverter
import com.cepgozcu.app.net.protocol.AlertKind
import com.cepgozcu.app.net.protocol.AlertSeverity
import com.cepgozcu.app.net.protocol.FileEventKind

/** Room can't store enums natively; we persist their wire name (stable, matches the protocol's SerialName) and parse back. */
class Converters {
    @TypeConverter
    fun fileEventKindToString(kind: FileEventKind): String = kind.name

    @TypeConverter
    fun stringToFileEventKind(value: String): FileEventKind = FileEventKind.valueOf(value)

    @TypeConverter
    fun alertKindToString(kind: AlertKind): String = kind.name

    @TypeConverter
    fun stringToAlertKind(value: String): AlertKind = AlertKind.valueOf(value)

    @TypeConverter
    fun alertSeverityToString(severity: AlertSeverity): String = severity.name

    @TypeConverter
    fun stringToAlertSeverity(value: String): AlertSeverity = AlertSeverity.valueOf(value)
}
