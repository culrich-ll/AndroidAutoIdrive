package me.hufman.androidautoidrive.music

import android.content.Context
import android.graphics.drawable.Drawable
import me.hufman.androidautoidrive.carapp.AMAppInfo
import me.hufman.androidautoidrive.carapp.AMAppInfo.Companion.getAppWeight
import me.hufman.androidautoidrive.carapp.AMCategory
import java.util.*

data class MusicAppInfo(override val name: String, override val icon: Drawable,
                        override val packageName: String, val className: String?): AMAppInfo {
	var probed = false
	var connectable = false     // whether MediaBrowser can connect
	var controllable = false    // whether MediaSession can control it
	var browseable = false      // whether any media items were discovered
	var searchable = false      // whether any search results came back
	var playsearchable = false  // whether the controller indicated PlayFromSearch support

	override val category = AMCategory.MULTIMEDIA

	var weightAdjustment = 0
	override val weight: Int
		get() {
			return 800 - (getAppWeight(this.name) - weightAdjustment)
		}

	// general helpers
	companion object {
		fun getInstance(context: Context, packageName: String, className: String?): MusicAppInfo {
			val packageManager = context.packageManager

			val appInfo = packageManager.getApplicationInfo(packageName, 0)
			val name = packageManager.getApplicationLabel(appInfo).toString()
			val icon = packageManager.getApplicationIcon(appInfo)
			return MusicAppInfo(name, icon, packageName, className)
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as MusicAppInfo

		if (name != other.name) return false
		if (packageName != other.packageName) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + packageName.hashCode()
		result = 31 * result + className.hashCode()
		return result
	}

	fun toMap():Map<String, Any?> {
		return mapOf(
				"name" to name,
				"packageName" to packageName,
				"className" to className,
				"connectable" to connectable,
				"controllable" to controllable,
				"browseable" to browseable,
				"searchable" to searchable,
				"playsearchable" to playsearchable
		)
	}

	fun clone(probed: Boolean? = null, connectable: Boolean? = null, controllable: Boolean? = null,
	          browseable: Boolean? = null, searchable: Boolean? = null, playsearchable: Boolean? = null,
	          weightAdjustment: Int? = null): MusicAppInfo {
		return MusicAppInfo(name, icon, packageName, className).also {
			it.probed = probed ?: this.probed
			it.connectable = connectable ?: this.connectable
			it.controllable = controllable ?: this.controllable
			it.browseable = browseable ?: this.browseable
			it.searchable = searchable ?: this.searchable
			it.playsearchable = playsearchable ?: this.playsearchable
			it.weightAdjustment = weightAdjustment ?: this.weightAdjustment
		}
	}

	override fun toString(): String {
		return "MusicAppInfo(name='$name', packageName='$packageName', className=$className, probed=$probed, connectable=$connectable, controllable=$controllable, browseable=$browseable, searchable=$searchable, playsearchable=$playsearchable)"
	}
}