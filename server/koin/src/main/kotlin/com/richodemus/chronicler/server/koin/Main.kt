package com.richodemus.chronicler.server.koin

import com.richodemus.chronicler.grpc.server.HelloWorldServer
import com.richodemus.chronicler.persistence.DiskEventPersister
import com.richodemus.chronicler.server.configuration.JavaPropertyConfiguration
import com.richodemus.chronicler.server.core.Chronicle
import com.richodemus.chronicler.server.core.Configuration
import com.richodemus.chronicler.server.core.Event
import com.richodemus.chronicler.server.core.EventCreationListener
import com.richodemus.chronicler.server.core.EventPersister
import org.koin.Logger.SLF4JLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.slf4j.LoggerFactory

object MyCoin {
    // todo split this into one module per gradle project so things can be internal again
    val Module = module {
        eagerSingle { Chronicle(get(), get()) }
        eagerSingle { Tezt() }
        eagerSingle { Dummy() as EventCreationListener }
        eagerSingle { DiskEventPersister(get()) as EventPersister }
        eagerSingle { JavaPropertyConfiguration() as Configuration }
        eagerSingle { HelloWorldServer() }
    }
}

fun main() {
    startKoin {
        // use Koin logger
        logger(SLF4JLogger())
        // declare modules
        modules(MyCoin.Module)
    }

//    System.`in`.read()
//    val test = Tezt()
//    println(test.chronicle.persister)
}

private inline fun <reified T> Module.eagerSingle(noinline definition: Definition<T>) {
    single(createdAtStart = true, definition = definition)
}

class Tezt : KoinComponent {
    internal val chronicle: Chronicle by inject()

    init {
        LoggerFactory.getLogger("Test").info("hello world!")
    }
}

internal class Dummy : EventCreationListener {
    override fun onEvent(event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

internal class Dummy2 : EventPersister {
    override fun persist(event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNumberOfEvents(): Int {
        return 0
    }

    override fun readEvents(): Iterator<Event> {
        return emptyList<Event>().iterator()
    }

}
