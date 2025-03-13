package kg.automoika.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import kg.automoika.cache.AppData
import kg.automoika.db.AuthDatabase
import kg.automoika.db.CarWashDatabase
import kg.automoika.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single<CarWashRepository> { CarWashRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val dataBaseModule = module {
    single { AppData() }
    single { CarWashDatabase }
    single { AuthDatabase }
}

fun mongoModule(uri : String, db : String) = module {
    single { MongoClient.create(uri)}
    single { get<MongoClient>().getDatabase(db) }
}

