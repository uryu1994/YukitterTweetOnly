package application;

import javafx.application.Platform;
import javafx.concurrent.Task;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.UserStreamAdapter;

public class MyUserStreamAdapter extends UserStreamAdapter {
	@Override
	public void onStatus(Status status) {
		super.onStatus(status);
		
		//--- if mention come ---//
		if("Guru_Yuki_mew".equals(status.getInReplyToScreenName())) {
			System.out.println("Reply!!!!");
			Platform.runLater( () -> {
				DialogManager.getInstance().createDialog(status);
			});
		}
	}
}