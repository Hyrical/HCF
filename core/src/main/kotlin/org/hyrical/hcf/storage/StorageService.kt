package org.hyrical.hcf.storage

import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.DatabaseFile
import org.hyrical.hcf.config.impl.StorageFile
import org.hyrical.store.connection.mongo.MongoConnection
import org.hyrical.store.connection.mongo.details.impl.AuthMongoDetails
import org.hyrical.store.connection.mongo.details.impl.NoAuthMongoDetails
import org.hyrical.store.connection.mongo.details.impl.URIMongoDetails
import org.hyrical.store.DataStoreController
import org.hyrical.store.Storable
import org.hyrical.store.connection.flatfile.FlatFileConnection
import org.hyrical.store.type.StorageType

object StorageService {

    lateinit var mongoConnection: MongoConnection

    fun start(){
        /*
        if (DatabaseFile.getBoolean("MONGO.AUTH.ENABLED")){
            connection = MongoConnection(AuthMongoDetails(
                DatabaseFile.getString("MONGO.HOST")!!,
                DatabaseFile.getInt("MONGO.PORT"),
                DatabaseFile.getString("MONGO.AUTH.USER")!!,
                DatabaseFile.getString("MONGO.AUTH.PASSWORD")!!,
            ), DatabaseFile.getString("MONGO.DATABASE")!!)
        } else if (DatabaseFile.getBoolean("MONGO.USE-URI.ENABLED")){
            connection = MongoConnection(
                URIMongoDetails(
                    DatabaseFile.getString("MONGO.USE-URI.URI")!!
                ), DatabaseFile.getString("MONGO.DATABASE")!!
            )
        } else {
            connection = MongoConnection(
                NoAuthMongoDetails(
                    DatabaseFile.getString("MONGO.HOST")!!,
                    DatabaseFile.getInt("MONGO.PORT"),
                ), DatabaseFile.getString("MONGO.DATABASE")!!
            )
        }


         */

        mongoConnection = MongoConnection(NoAuthMongoDetails(), database = "HCF")


    }

    inline fun <reified T : Storable> getRepository(key: String): DataStoreController<T> {
        return DataStoreController.of(
            if (StorageFile.getString(key) == "MONGO") StorageType.MONGO else StorageType.FLAT_FILE,
            if (StorageFile.getString(key) == "MONGO") {
                mongoConnection
            } else {
                FlatFileConnection(HCFPlugin.instance.dataFolder.absolutePath, key.lowercase())
            },
            false,
            HCFPlugin.instance.logger
        )
    }
}