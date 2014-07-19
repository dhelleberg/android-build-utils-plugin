package de.inovex.build.androidutils

import groovy.transform.ToString

/**
 * Created by dhelleberg on 18/07/14.
 */
@ToString(includeNames=true)
class AndroidBuildUtilsPluginExtension {

    //config fields for the DSL
    Boolean fileVersions = true
    Boolean buildConfigFields = true

    String timeZone = "UTC"

    //set version from DSL
    Integer major = 1
    Integer minor = 0
    Integer patch = 0

}
