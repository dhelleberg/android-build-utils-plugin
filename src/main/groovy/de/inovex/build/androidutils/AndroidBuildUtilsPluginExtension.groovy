package de.inovex.build.androidutils

import groovy.transform.ToString

/**
 * Created by dhelleberg on 18/07/14.
 */
@ToString(includeNames=true)
class AndroidBuildUtilsPluginExtension {

    def PATTERNS = [  "APPLICATION_NAME" : "%a",
                      "FLAVOR" : "%f",
                      "BUILDTYE" : "%bt",
                      "SIGNED" : "%s",
                      "VERSION_MAJOR" : "%vmaj",
                      "VERSION_MINOR" : "%vmin",
                      "VERSION_BUILD" : "%vb"   ]

    final String FILENAME_DEVIDER = "-"
    final String FILENAME_SUFFIX = ".apk"

    //config fields for the DSL
    Boolean versioning = true
    Boolean buildConfigFields = true

    Boolean setVersionCode = true
    Boolean setVersionName = true
    Boolean versionFileName = true

    String timeZone = "UTC"

    //set version from DSL
    Integer major = 1
    Integer minor = 0
    Integer patch = 0

    //APK-Filename-default-pattern
    String apknamePattern = PATTERNS.APPLICATION_NAME + FILENAME_DEVIDER + PATTERNS.FLAVOR + FILENAME_DEVIDER +
                            PATTERNS.BUILDTYE + FILENAME_DEVIDER + PATTERNS.SIGNED + FILENAME_DEVIDER +
                            PATTERNS.VERSION_MAJOR + "." + PATTERNS.VERSION_MINOR + "." + PATTERNS.BUILDTYPE +
                            FILENAME_SUFFIX


}
