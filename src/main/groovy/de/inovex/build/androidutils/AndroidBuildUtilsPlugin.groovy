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
        log.quiet("version $project.android.defaultConfig")
        //get extension config from DSL
        def extension = project.extensions.findByName(EXTENSION_NAME)

        log.quiet(">! extension found: $extension")

        project.afterEvaluate {

            appName = project.name
            if(extension.buildConfigFields)
                processBuildConfigFlags(project, extension)

            if(extension.versioning)
                processVersioning(project, extension)
        }
    }

    def processVersioning(Project project, AndroidBuildUtilsPluginExtension extension) {
        //set version name and versioncode

        AppVersionInformation appVersionInformation = new AppVersionInformation(extension.major, extension.minor, extension.patch)

        ProductFlavorDsl defConfig = project.android.defaultConfig

        if(extension.setVersionCode)
            defConfig.versionCode = appVersionInformation.versionCode
        if(extension.setVersionName)
            defConfig.versionName = appVersionInformation.versionName

        project.android.applicationVariants.all { variant ->
            if(extension.setVersionName)
                variant.mergedFlavor.versionName = appVersionInformation.versionName
            if(extension.setVersionCode)
                variant.mergedFlavor.versionCode = appVersionInformation.versionCode

            if(extension.versionFileName) {
                def apkSuffix = "-${variant.mergedFlavor.versionName}"
                def originalApkFile = variant.outputFile
                log.quiet("variant: $variant.mergedFlavor apkSuffix: $apkSuffix origName $originalApkFile")
                log.quiet("-> $project.archivesBaseName WHAT $variantData.variantConfiguration.baseName")
                log.quiet("is signed $variantData.signed()")


                def signed = variantData.signed ? "signed" : "unsigned"
                def PATTERNS = extension.PATTERNS
                String newAPKname = extension.apknamePattern;
                //create new APK Filename
                newAPKname = newAPKname.replaceAll(PATTERNS.APPLICATION_NAME, project.archivesBaseName);
                newAPKname = newAPKname.replaceAll(PATTERNS.BUILDTYE, variantData.variantConfiguration.buildType.name);
                newAPKname = newAPKname.replaceAll(PATTERNS.FLAVOR, variantData.variantConfiguration.flavorName);
                newAPKname = newAPKname.replaceAll(PATTERNS.SIGNED , signed);


                log.quiet("newAPKName: $newAPKname")

//                def newApkFile = new File(originalApkFile.getParent(),  originalApkFile.name.replace(".apk", "${apkSuffix}.apk"))
                def newApkFile = new File(originalApkFile.getParent(),  newAPKname)
                variant.outputFile = newApkFile
            }

        }

/*
        project.android.libraryVariants.all { variant ->

            log.quiet("!!! $project.archivesBaseName WHAT $variantData.variantConfiguration.baseName")
        }Í
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


}
