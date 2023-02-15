package org.hyrical.webapi.storage

import org.hyrical.store.DataStoreController
import org.hyrical.store.connection.flatfile.FlatFileConnection
import org.hyrical.store.type.StorageType
import org.hyrical.webapi.type.Licence

object StorageHandler {
    val controller = DataStoreController.of<Licence>(StorageType.FLAT_FILE, FlatFileConnection(this::class.java.protectionDomain.codeSource.location.toURI().path, "licences"))

    fun getLicence(identifier: String): Licence? {
        return controller.repository.search(identifier)
    }
}