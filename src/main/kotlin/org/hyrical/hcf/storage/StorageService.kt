package org.hyrical.hcf.storage

import org.hyrical.store.connection.mongo.MongoConnection
import org.hyrical.store.connection.mongo.details.impl.URIMongoDetails

object StorageService {

    var connection: MongoConnection = MongoConnection(
        URIMongoDetails("mongodb://localhost:27017"),
        "Gens"
    )
}