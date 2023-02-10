package org.hyrical.hcf.storage

import org.hyrical.hcf.config.impl.DatabaseFile
import org.hyrical.store.connection.mongo.MongoConnection
import org.hyrical.store.connection.mongo.details.impl.AuthMongoDetails
import org.hyrical.store.connection.mongo.details.impl.NoAuthMongoDetails
import org.hyrical.store.connection.mongo.details.impl.URIMongoDetails

object StorageService {

    lateinit var connection: MongoConnection

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

        connection = MongoConnection(NoAuthMongoDetails(), database = "HCF")

    }
}