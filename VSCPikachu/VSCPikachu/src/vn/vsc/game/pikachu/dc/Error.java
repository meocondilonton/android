
package vn.vsc.game.pikachu.dc;

import vn.vsc.game.pikachu.KaApp;
import vn.vsc.game.pikachu.R;


public enum Error {
	NETWORK_NO_CONNECTION, NETWORK_TIMED_OUT, NETWORK_ERROR, SERVER_ERROR, UNKNOWN_ERROR,CARD_NOT_VALID,CARD_USED;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (this) {
		case NETWORK_NO_CONNECTION:
			return KaApp.getAppContext().getString(
					R.string.error_no_connection);
		case NETWORK_TIMED_OUT:
			return KaApp.getAppContext().getString(
					R.string.error_connection_time_out);
		case NETWORK_ERROR:
			return KaApp.getAppContext().getString(
					R.string.error_network_error);
		case SERVER_ERROR:
			return KaApp.getAppContext().getString(
					R.string.error_server_error);
		case UNKNOWN_ERROR:
			return KaApp.getAppContext().getString(
					R.string.error_unknown_error);
		case CARD_NOT_VALID:
			return KaApp.getAppContext().getString(
					R.string.error_card_invalid);
		case CARD_USED:
			return KaApp.getAppContext().getString(
					R.string.error_card_used);
		default:
			return "";
		}
	}

}
