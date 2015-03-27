package twitterUtil;

import application.MainController;
import manager.DialogManager;
import javafx.application.Platform;
import serialize.OAuthConfiguration;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamAdapter;

public class MyUserStreamAdapter extends UserStreamAdapter {
	private static String screenName;
	
	/**
	 * スクリーンネームをメンバ変数に保持するコンストラクタ
	 * 
	 */
	public MyUserStreamAdapter () {
			screenName = TwitterUtil.getScreenName();
	}

	/**
	 * ツイートを受信した時に呼び出される
	 * 
	 * @param status ツイートの情報
	 */
	@Override
	public void onStatus(Status status) {
		super.onStatus(status);

		MainController.getInstance().addStatus(status);

		//--- メンションを受信した時の処理 ---//
		if(screenName.equals(status.getInReplyToScreenName())) {
			Platform.runLater( () -> {
				DialogManager.getSingleton().createDialog(status, status.getUser(), "reply");
			});
		}
		
		//-- リツイートされた時の処理 --//
		if(status.isRetweet()) {
			if(screenName.equals(status.getRetweetedStatus().getUser().getScreenName())) {
				Platform.runLater( () -> {
					DialogManager.getSingleton().createDialog(status.getRetweetedStatus(), status.getUser(), "retweet");
				});
			}
		}

//		System.out.println("[debug] status information is >> " + status);
//		System.out.println("[debug] status.getRetweetedStatus is >> " + status.getRetweetedStatus() );
	}
	
	/**
	 * ツイートをお気に入りされた時に呼び出される
	 * 
	 * @param source ツイートをお気に入りしたユーザー
	 * @param target ツイートをお気に入りされたユーザー
	 * @param favoriteStatus お気に入りされたツイート
	 */
	@Override
	public void onFavorite(User source, User target, Status favoritedStatus) {
		super.onFavorite(source,target,favoritedStatus);

		if(screenName.equals(target.getScreenName())) {
			Platform.runLater( () -> {
				DialogManager.getSingleton().createDialog(favoritedStatus, source, "favorite");
			});
		}
		autoMecaotaReply(source);
		
//		System.out.println("[debug] お気に入り後のステータス = [[ " + favoritedStatus + " ]]");
//		System.out.println("[info]" + source.getName() + "が" + target.getName() + "のツイート「" + favoritedStatus.getText() + "」をふぁぼった");
	}
	
	/**
	 * ツイートのお気に入りを解除された時に呼び出される
	 * 
	 * @param source ツイートのお気に入りを解除したユーザー
	 * @param target ツイートのお気に入りを解除されたユーザー
	 * @param favoriteStatus お気に入りを解除されたツイート
	 */
	@Override
	public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
		super.onUnfavorite(source,target,unfavoritedStatus);
		
		System.out.println("[info]" + source.getName() + "が" + target.getName() + "のツイート「" + unfavoritedStatus.getText() + "」のふぁぼを解除した");
	}
	
	/**
	 * ユーザをブロックした時に呼び出される
	 * 
	 * @param source ブロックしたユーザー：自分
	 * @param blockedUser ブロックされたユーザー：相手
	 */
	@Override
	public void onBlock(User source, User blockedUser) {
		
	}
	
	/**
	 * ダイレクトメッセージを受信した時に呼び出される
	 * 
	 * @param directMessage 
	 */
	@Override
	public void onDirectMessage(DirectMessage directMessage) {
		
	}
	
	/**
	 * ユーザーをフォローした時に呼び出される
	 * 
	 * @Param source フォローしたユーザー：自分
	 * @param followedUser フォローされたユーザー：相手
	 */
	@Override
	public void onFollow(User source, User followedUser) {
		
	}
	
	/**
	 * フォローリストを取得した時に呼び出される
	 * 
	 * @param friendIds フォローしているユーザー情報の配列
	 */
	@Override
	public void onFriendList(long[] friendIds) {
		
	}
	
	/**
	 * ブロックを解除した時に呼び出される
	 * 
	 * @param source ブロックを解除した側のユーザー：自分
	 * @param unblockedUser ブロックを解除された側のユーザー：自分
	 */
	@Override
	public void onUnblock(User source, User unblockedUser) {
		
	}
	
	/**
	 * フォローを外した時に呼び出される
	 * 
	 * @param source フォローを外した側のユーザー：自分
	 * @param unfollowedUser フォローを外された側のユーザー：相手
	 */
	@Override
	public void onUnfollow(User source, User unfollowedUser) {
		
	}

	/**
	 * リストを作成した時に呼び出される
	 * 
	 * @param listOwner ストを作成したユーザー：自分
	 * @param list 作成するリスト
	 */
	@Override
	public void onUserListCreation(User listOwner, UserList list) {
		
	}
	
	/**
	 * リストを削除した時に呼び出される
	 * @param listOwner リストを作成したユーザー：自分
	 * @param list 削除するリスト
	 */
	@Override
	public void onUserListDeletion(User listOwner, UserList list) {
		
	}

	/**
	 * リストにユーザーを追加する時に呼び出される
	 * 
	 * @param addedMemver リストに追加されたユーザー
	 * @param listOwner リストに追加したユーザー
	 * @param list ユーザーを追加するリスト
	 */
	@Override
	public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
		
	}

	/**
	 * リストからユーザーを削除する時に呼び出される
	 * 
	 * @param deletedMemver リストから削除されたユーザー
	 * @param listOwner リストから削除したユーザー
	 * @param list ユーザーを削除するリスト
	 */
	@Override
	public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
		
	}

	/**
	 * リストの購読申請をした時に呼び出される
	 * @param subscriber リストの購読申請を行ったユーザー
	 * @param listOwner 購読申請をされたリストを持っているユーザー
	 * @param list 購読を申請されたリスト
	 */
	@Override
	public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
		
	}

	/**
	 * リストの購読の解除申請をした時に呼び出される
	 * @param subscriber リストの購読の解除申請を行ったユーザー
	 * @param listOwner 購読の解除申請をされたリストを持っているユーザー
	 * @param list 購読解除を申請されたリスト
	 */
	@Override
	public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
		
	}

	/**
	 * プロファイルを更新した際に呼び出される
	 * 
	 * @param updatedUser 変更後のユーザー情報
	 */
	@Override
	public void onUserProfileUpdate(User updatedUser) {
		
	}
	
	/**
	 * メカオタくんにツイートをお気に入りされた時に呼び出される
	 * @param source
	 */
	private void autoMecaotaReply(User source) {
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
	
	}
	
}