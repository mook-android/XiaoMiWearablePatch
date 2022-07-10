# XiaoMiWearablePatch

This Xposed module patches the Mi Fitness application (`com.xiaomi.wearable`) to work around a bug
where it will fail to list your devices.

## Technical Details

Xiaomi uses the Android [Room] library for handling storage; however, their implementation (as of
version 3.5.1i) has a bug where they access it too early.  This means that when Room internally
tries to get the package name, it fails because it couldn't find the application context.  This
patch catches this and returns the context it has (which happens to be a partially initialized
`Application`) to work around the issue.

The relevant stack looks something like:
```
FATAL EXCEPTION: Thread-2
Process: com.xiaomi.wearable:device, PID: 5467
java.lang.ExceptionInInitializerError
	at com.xiaomi.fitness.settingitem.DeviceSettingManager.getDatabase(SourceFile:1)
	at com.xiaomi.fitness.settingitem.DeviceSettingManager.readSettingItemValue(SourceFile:1)
	at com.xiaomi.fitness.health.curse.CurseManager.readSetting(SourceFile:1)
	at com.xiaomi.fitness.health.curse.CurseManager.initSetting(SourceFile:2)
...
Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String android.content.Context.getPackageName()' on a null object reference
	at android.content.ComponentName.<init>(ComponentName.java:131)
	at android.content.Intent.<init>(Intent.java:6875)
	at kk0.<init>(SourceFile:13)
	at jk0.n(SourceFile:2)
	at androidx.room.RoomDatabase.init(SourceFile:17)
	at androidx.room.RoomDatabase$a.d(SourceFile:30)
...
```

[Room]: https://developer.android.com/training/data-storage/room
