import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.Watcher.Event.EventType.*
import org.apache.zookeeper.ZooKeeper
import kotlin.system.exitProcess

const val NODE_PATH = "/z"

class ZookeeperConnection(host: String, watcher: ZookeeperWatcher) {
    val zk: ZooKeeper = ZooKeeper(host, 3000, watcher)

    fun run() {
        while(true) {
            when(readLine()) {
                "printTree" -> printTree(NODE_PATH)
                "quit" -> exitProcess(0)
            }
        }
    }

    private fun printTree(path: String) {
        if (zk.exists(path, false) != null) {
            println(path)
            zk.getChildren(path, null).forEach {
                printTree("$path/$it")
            }
        }
    }
}

class ZookeeperWatcher() : Watcher {
    private var childrenCount = 0
    private lateinit var zk: ZooKeeper

    fun defineZookeeper(zooKeeper: ZooKeeper){
        zk = zooKeeper
        if (zk.exists(NODE_PATH, this) == null)
            waitForNode();
        else {
            waitForNode();
            countChildrenNum(NODE_PATH);
        }
    }

    override fun process(event: WatchedEvent?) {
        println(event)
        if(event?.path == NODE_PATH){
            when(event.type) {
                NodeCreated -> {
                    waitForNode()
                    runChess()
                }
                NodeDeleted -> {
                    stopChess()
                    waitForNode()
                    childrenCount = 0
                }
                NodeChildrenChanged -> {
                    countChildrenNum(NODE_PATH)
                    println(childrenCount)
                    waitForNode()
                    childrenCount = 0
                }
                else -> {}
            }
        }
    }

    private fun runChess() {
        Runtime.getRuntime().exec("open -a Chess")
    }

    private fun stopChess() {
        Runtime.getRuntime().exec("pkill -x Chess")
    }

    private fun countChildrenNum(path: String){
        zk.getChildren(path, false).forEach {
            childrenCount++
            countChildrenNum("$path/$it")
        }
    }

    private fun waitForNode() {
        zk.exists(NODE_PATH, this, null, null)
        if(zk.exists(NODE_PATH, false) != null)
            watchDescendants(NODE_PATH)
    }

    private fun watchDescendants(path: String) {
        zk.getChildren(path, this)
        zk.getChildren(path, this).forEach {
            watchDescendants("$path/$it")
        }
    }
}


fun main() {
    val watcher = ZookeeperWatcher()
    val connection = ZookeeperConnection("127.0.0.1:2181", watcher)
    watcher.defineZookeeper(connection.zk)
    connection.run()
}