/*
 *  Copyright 2026 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

val rootProjectName: String by settings
val group: String by settings
val packageOrg: String by settings
val packageRepo: String by settings
val catalogOrg: String by settings
val catalogGroup: String by settings
val catalogRepo: String by settings
val catalogName: String by settings

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://$catalogOrg.github.io/$catalogRepo/")
        }
    }
    versionCatalogs {
        create("libs") {
            from("$catalogGroup:$catalogName")
        }
    }
}

rootProject.name = rootProjectName
