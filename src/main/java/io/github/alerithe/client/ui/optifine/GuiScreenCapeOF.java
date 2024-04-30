package io.github.alerithe.client.ui.optifine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.math.BigInteger;
import java.net.URI;
import java.util.Random;

public class GuiScreenCapeOF extends GuiScreen {
    private final GuiScreen parentScreen;
    private String message;
    private long messageHideTimeMs;
    private String linkUrl;
    private GuiButton buttonCopyLink;

    public GuiScreenCapeOF(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    @Override
    public void initGui() {
        int i = 0;
        this.buttonList.add(new GuiButton(210, this.width / 2 - 155, this.height / 6 + 24 * ((i += 2) >> 1), 150, 20,
                "Open Cape Editor"));
        this.buttonList.add(new GuiButton(220, this.width / 2 - 155 + 160, this.height / 6 + 24 * (i >> 1), 150, 20,
                "Reload Cape"));
        this.buttonCopyLink = new GuiButton(230, this.width / 2 - 100, this.height / 6 + 24 * ((i += 6) >> 1), 200, 20,
                "Copy Editor Link");
        this.buttonCopyLink.visible = this.linkUrl != null;
        this.buttonList.add(this.buttonCopyLink);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * ((i += 4) >> 1),
                I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 200:
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;
                case 210:
                    try {
                        BigInteger random1Bi = new BigInteger(128, new Random());
                        BigInteger random2Bi = new BigInteger(128, new Random(System.identityHashCode(new Object())));
                        BigInteger serverBi = random1Bi.xor(random2Bi);
                        String serverId = serverBi.toString(16);
                        this.mc.getSessionService().joinServer(this.mc.getSession().getProfile(),
                                this.mc.getSession().getToken(), serverId);
                        String urlStr = "https://optifine.net/capeChange?u="
                                + this.mc.getSession().getProfile().getId().toString().replace("-", "")
                                + "&n=" + this.mc.getSession().getProfile().getName() + "&s=" + serverId;
                        if (this.openWebLink(new URI(urlStr))) {
                            this.showMessage("The OptiFine cape editor should open in a web browser.", 10000L);
                        } else {
                            this.showMessage("Error opening OptiFine cape editor.", 10000L);
                            this.setLinkUrl(urlStr);
                            this.buttonCopyLink.visible = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 220:
                    this.showMessage("The cape will be reloaded in 15 seconds.", 15000L);
                    if (this.mc.player != null) {
                        long delayMs = 15000L;
                        long reloadTimeMs = System.currentTimeMillis() + delayMs;
                        this.mc.player.setReloadCapeTimeMs(reloadTimeMs);
                    }
                    break;
                case 230:
                    if (this.linkUrl != null) GuiScreen.setClipboardString(this.linkUrl);
                    break;
            }
        }
    }

    private void showMessage(String msg, long timeMs) {
        this.message = msg;
        this.messageHideTimeMs = System.currentTimeMillis() + timeMs;
        this.setLinkUrl(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "OptiFine Cape", this.width / 2, 20, 0xFFFFFF);

        if (this.message != null) {
            this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.height / 6 + 60, 0xFFFFFF);
            if (System.currentTimeMillis() > this.messageHideTimeMs) {
                this.message = null;
                this.setLinkUrl(null);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
        this.buttonCopyLink.visible = linkUrl != null;
    }
}
 