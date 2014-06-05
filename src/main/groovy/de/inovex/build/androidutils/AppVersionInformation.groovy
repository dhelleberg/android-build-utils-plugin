package de.inovex.build.androidutils

/**
 * Created by dhelleberg on 28/05/14.
 */
class AppVersionInformation {
    int minorVersion
    int majorVersion
    int patchVersion

    int versionCode

    String versionName

    public AppVersionInformation(int major, int minor, int patch) {
        majorVersion = major
        minorVersion = minor
        patchVersion = patch

        versionCode = majorVersion * 1000 + minorVersion * 100 + patchVersion * 10
        versionName = "$major.$minor.$patch"
    }

    @Override
    String toString() {
        getVersionName()+"-$versionCode"
    }

}
