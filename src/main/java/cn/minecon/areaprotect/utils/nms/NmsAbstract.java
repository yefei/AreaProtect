package cn.minecon.areaprotect.utils.nms;

import cn.minecon.areaprotect.utils.Log;
import cn.minecon.areaprotect.utils.nms.ReflectionUtils.PackageType;

public abstract class NmsAbstract
{
	private static int version;
	private static boolean initialized;
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final int DEFAULT_REQUIRED_VERSION = 7;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean available = false;
	public boolean isAvailable() { return this.available; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public NmsAbstract() {
		try {
			if (this.isAvailableForCurrentVersion()) {
				setup();
				available = true;
			}
		} catch (Throwable t) {
			String errorMsg = String.format("If you use 1.%s.X or above", this.getRequiredVersion());
			Log.warning(errorMsg);
			t.printStackTrace();
			available = false;
		}
	}
	
	protected abstract void setup() throws Throwable;
	
	// -------------------------------------------- //
	// VERSIONING
	// -------------------------------------------- //

	public static int getCurrentVersion() {
		if (!initialized) {
			version = Integer.parseInt(Character.toString(PackageType.getServerVersion().charAt(3)));
			initialized = true;
		}
		return version;
	}
	
	public int getRequiredVersion() {
		return DEFAULT_REQUIRED_VERSION;
	}
	
	public boolean isAvailableForCurrentVersion() {
		return getCurrentVersion() >= this.getRequiredVersion();
	}
	
}
