package de.inovex.build.androidutils

import com.android.build.gradle.internal.dsl.ProductFlavorDsl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

/**
 * Created by dhelleberg on 29/04/14.
 */
class AndroidBuildUtilsPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'androidBuildUtils'


    final Logger log = Logging.getLogger AndroidBuildUtilsPlugin


    //values for the build_config fields
    def hostName = "unknow" //will be set later


    //version TODO: Get this from git
    def version = "2.3.1"
    def appName = "unknown"

    @Override
    void apply(Project project) {
        log.quiet(">applying plugin $this")

        project.extensions.create(EXTENSION_NAME, AndroidBuildUtilsPluginExtension)

        project.afterEvaluate {
            log.quiet("version $project.android.defaultConfig")
            //get extension config from DSL
            def extension = project.extensions.findByName(EXTENSION_NAME)
            log.quiet("extension found: $extension")

            appName = project.name
            if(extension.buildConfigFields)
                processBuildConfigFlags(project, extension)

            if(extension.fileVersions)
                processVersioning(project, extension)
        }
    }

    def processVersioning(Project project, AndroidBuildUtilsPluginExtension extension) {
        //set version name and versioncode

        AppVersionInformation appVersionInformation = readVersion(project)

        ProductFlavorDsl defConfig = project.android.defaultConfig
        defConfig.versionCode = appVersionInformation.versionCode
        defConfig.versionName = appVersionInformation.versionName


        project.android.applicationVariants.all { variant ->
            variant.mergedFlavor.versionName = appVersionInformation.versionName
            variant.mergedFlavor.versionCode = appVersionInformation.versionCode

            def apkSuffix = "-${variant.mergedFlavor.versionName}"
            def originalApkFile = variant.outputFile
            log.quiet("variant: $variant apkSuffix: $apkSuffix origName $originalApkFile")
            log.quiet("-> $project.archivesBaseName WHAT $variantData.variantConfiguration.baseName")
            log.quiet("is signed $variantData.signed()")

            def newApkFile = new File(originalApkFile.getParent(),  originalApkFile.name.replace(".apk", "${apkSuffix}.apk"))
            variant.outputFile = newApkFile
        }

/*
        project.android.libraryVariants.all { variant ->

            log.quiet("!!! $project.archivesBaseName WHAT $variantData.variantConfiguration.baseName")
        }
*/

        project.android.testVariants.all { variant ->

            log.quiet("!!! $project.archivesBaseName WHAT $variantData.variantConfiguration.baseName")
        }

    }



    def processBuildConfigFlags(Project project, AndroidBuildUtilsPluginExtension extension) {
        ProductFlavorDsl defConfig = project.android.defaultConfig

        try {
            hostName = java.net.InetAddress.getLocalHost().getHostName()
        } catch(Exception e) {
            log.debug("could not determine hostname",e)
        }
        def buildTime = new Date().format("yyyy-MM-dd'|'HH:mm|Z", TimeZone.getTimeZone(extension.timeZone))

        defConfig.buildConfigField("String", "BUILD_DATE", "\"$buildTime\"")
        defConfig.buildConfigField("String", "BUILD_HOST", "\"$hostName\"")
    }


    AppVersionInformation readVersion(Project project) {

        def rootProject = project.getRootProject()

        log.quiet 'using version: '+rootProject.version

        AppVersionInformation appVersionInformation = new AppVersionInformation(3,1,2)

        log.quiet("appVersion: "+appVersionInformation)
        return appVersionInformation
    }

}
