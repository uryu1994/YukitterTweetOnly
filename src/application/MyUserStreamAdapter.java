package application;

import javafx.application.Platform;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
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
	
	@Override
	public void onFavorite(User source, User target, Status favoritedStatus)
	{
		super.onFavorite(source,target,favoritedStatus);
		
		if("mecaota".equals(source.getScreenName()) ) {
			try {
				TwitterFactory.getSingleton().updateStatus("@mecaota ちっす、Yuki猫でーすwww、いつもふぁぼあざーっすwwww");
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(source.getName() + "が" + target.getName() + "のツイート「" + favoritedStatus.getText() + "」をふぁぼった");
	}
}