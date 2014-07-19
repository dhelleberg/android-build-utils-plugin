package de.inovex.build.androidutils

import groovy.transform.ToString

/**
 * Created by dhelleberg on 18/07/14.
 */
@ToString(includeNames=true)
class AndroidBuildUtilsPluginExtension {

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

}
