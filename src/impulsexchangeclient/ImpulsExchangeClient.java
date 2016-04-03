package impulsexchangeclient;

import impulsexchangeclient.common.FBServerManager;
import impulsexchangeclient.options.OptionsHandler;

public class ImpulsExchangeClient {

    public static void main(String[] args) {
        OptionsHandler.readOptions();
        FBServerManager.getInstance().run();
        new FrameMain().setVisible(true);
    }
}
