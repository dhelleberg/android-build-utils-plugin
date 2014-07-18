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

    final Logger log = Logging.getLogger AndroidBuildUtilsPlugin


    //values for the build_config fields
    def buildTime = new Date().format("yyyy-MM-dd'|'HH:mm|Z", TimeZone.getTimeZone("UTC"))
    def hostName = "unknow" //will be set later

    //config stuff TODO: get it via DSL
    def setBuildConfigFields = true
    def doVersioning = true

    //version TODO: Get this from git
    def version = "2.3.1"
    def appName = "unknown"

    @Override
    void apply(Project project) {
        log.quiet("applying plugin $this")

        project.afterEvaluate {
            log.quiet("version $project.android.defaultConfig")
            appName = project.name
            if(setBuildConfigFields)
                processBuildConfigFlags(project)
            if(doVersioning)
                processVersioning(project)
        }
    }

    def processVersioning(Project project) {
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



    def processBuildConfigFlags(Project project) {
        ProductFlavorDsl defConfig = project.android.defaultConfig

        try {
            hostName = java.net.InetAddress.getLocalHost().getHostName()
        } catch(Exception e) {
            log.debug("could not determine hostname",e)
        }


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
