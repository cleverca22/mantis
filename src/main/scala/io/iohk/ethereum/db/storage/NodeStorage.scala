package io.iohk.ethereum.db.storage

import akka.util.ByteString
import io.iohk.ethereum.db.dataSource.DataSource
import io.iohk.ethereum.db.storage.NodeStorage._
import io.iohk.ethereum.mpt.NodesKeyValueStorage

/**
  * This class is used to store Nodes (defined in mpt/Node.scala), by using:
  *   Key: hash of the RLP encoded node
  *   Value: the RLP encoded node
  */
class NodeStorage(val dataSource: DataSource) extends KeyValueStorage[NodeHash, NodeEncoded, NodeStorage] {

  val namespace: IndexedSeq[Byte] = Namespaces.NodeNamespace
  def keySerializer: NodeHash => IndexedSeq[Byte] = _.toIndexedSeq
  def valueSerializer: NodeEncoded => IndexedSeq[Byte] = _.toIndexedSeq
  def valueDeserializer: IndexedSeq[Byte] => NodeEncoded = _.toArray

  protected def apply(dataSource: DataSource): NodeStorage = new NodeStorage(dataSource)
}

object NodeStorage {
  type NodeHash = ByteString
  type NodeEncoded = Array[Byte]
}
