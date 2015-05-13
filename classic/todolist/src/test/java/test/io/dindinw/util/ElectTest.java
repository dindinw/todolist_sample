package test.io.dindinw.util;

/**
 * A conception of leader election.
 *
 * A Quorum is the minimum number of required persons for a agreement of a vote. etc. 3 -> 2 5 -> 3
 *
 * It's the minimum server number of servers that it's running and available which store a client's
 * data before telling the client that your data is safe to store.
 *
 * Think the example, we have 3 servers node. node-1 is down. node-2 and node-3 is working and have
 * saved some data. then node-1 is back. now replication is required to update node-1 to the latest
 * from node-2 and 3. But How we know to update node-1 by using node-2 and node-3 instead of reversing?
 * Because quorum node-2 and node-3. so that we know the leader must in node-2,node-3. the node-1 must
 * a follower.
 *
 * S1,S2,S3,S4,S5, for any request, need 3 as the majority out of the five servers in the ensemble.
 * That's say for any request of update, it's required at least of three server acknowledge that the
 * state changes has been replicated before response that the update_of_state has been completed.
 *
 * Quorums must guarantee that, regardless of delays and crashes in the system, any update request
 * the service positively acknowledges will persist until another request supersedes it.
 *
 * When Quorum become larger, which implies that we need more acknowledgments for each request.
 *
 * The leader is a server that has been chosen by an ensemble of servers.
 * The leader transforms each request into a transaction, and apply it to the followers in the order
 * issued by the leader.
 */
public class ElectTest {
}
