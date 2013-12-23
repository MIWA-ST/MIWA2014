package fr.epita.sigl.miwa.st.sync;

public class SyncMessFactory {
	static public ISyncMessSender getSyncMessSender () {
		return SyncMessSender.getInstance();
	}
	
	static public void initSyncMessReceiver() {
		SyncMessReceiver.getInstance();
	}
}
