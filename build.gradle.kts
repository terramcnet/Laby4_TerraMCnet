import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")
val addonVersion = "1.5.0"

group = "net.terramc"
version = providers.environmentVariable("VERSION").getOrElse(addonVersion)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    defaultCharacterEncoding = "UTF-8"
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}

labyMod {
    defaultPackageName = "net.terramc.addon" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "terramc"
        displayName = "TerraMCnet"
        author = "MisterCore"
        description = "This addon offers you advanced information on TerraMC.net. For example it will show you your current nickname, game rank and much more."
        minecraftVersion = "*"
        version = System.getenv().getOrDefault("VERSION", addonVersion)
        releaseChannel = ReleaseChannels.SNAPSHOT
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
