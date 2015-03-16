package application;

import twitter4j.Status;
import twitter4j.UserStreamAdapter;

public class MyUserStreamAdapter extends UserStreamAdapter {
	@Override
	public void onStatus(Status status) {
		super.onStatus(status);
		
		//--- if mention come ---//
		if(status.getInReplyToScreenName().equals("guru_yuki_mew")) {
			System.out.println("Reply!!!!");
		}
		
//		mainController.observableList.add(status);
		System.out.println(status.getUser().getName() + "@" + status.getUser().getScreenName() + ") >> " + status.getText());
   	}
}