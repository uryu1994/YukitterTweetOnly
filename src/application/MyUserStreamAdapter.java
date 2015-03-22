package application;

import javafx.application.Platform;
import serialize.OAuthConfiguration;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserStreamAdapter;

public class MyUserStreamAdapter extends UserStreamAdapter {
	private static String screenName;
	
	public MyUserStreamAdapter () {
		try {
			screenName = MainController.getInstance().getTwitter().getScreenName();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStatus(Status status) {
		super.onStatus(status);
		
		MainController.getInstance().addStatus(status);
		
		//--- if mention come ---//
		if(screenName.equals(status.getInReplyToScreenName())) {
			System.out.println("Reply!!!!");
			Platform.runLater( () -> {
				DialogManager.getInstance().createDialog(status, "skyBlue");
			});
		}
	}
	
	@Override
	public void onFavorite(User source, User target, Status favoritedStatus) {
		super.onFavorite(source,target,favoritedStatus);
		
		if("mecaota".equals(source.getScreenName()) ) {
			try {
				new TwitterFactory()
				.getInstance(OAuthConfiguration.createConfiguration())
				.updateStatus("@mecaota ちっす、Yuki猫でーすwww、いつもふぁぼあざーっすwwww");
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Platform.runLater( () -> {
			DialogManager.getInstance().createDialog(favoritedStatus, "khaki");		
		});
		System.out.println(source.getName() + "が" + target.getName() + "のツイート「" + favoritedStatus.getText() + "」をふぁぼった");
	}
}