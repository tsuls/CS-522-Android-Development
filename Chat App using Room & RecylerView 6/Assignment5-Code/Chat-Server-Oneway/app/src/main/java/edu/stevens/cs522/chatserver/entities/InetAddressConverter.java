package edu.stevens.cs522.chatserver.entities;

import androidx.room.TypeConverter;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.base.InetAddressUtils;

// TODO complete this class (use InetAddressUtils)
public class InetAddressConverter {

    @TypeConverter
    public static InetAddress fromString(String value) {
        return value == null ? null : InetAddressUtils.fromString(value.replace("/", ""));
    }

    @TypeConverter
    public static String toString(InetAddress value) {
        return value == null ? null : value.toString();
    }


}
