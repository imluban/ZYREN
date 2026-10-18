# Keep classes and constructors looked up from jni_bridge.cpp.
-keep class com.lubannoor.pulse.usbaudio.model.DacFormat { <init>(...); }
-keep class com.lubannoor.pulse.usbaudio.model.DacCapabilities { <init>(...); }

# Keep names used by the exported JNI method symbols.
-keepclasseswithmembernames class com.lubannoor.pulse.usbaudio.UsbAudioDriver { native <methods>; }
