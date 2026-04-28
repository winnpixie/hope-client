package io.github.alerithe.client.ui.optifine;

import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.math.BigInteger;
import java.net.URI;
import java.util.Random;

public class GuiScreenCapeOF extends GuiScreen {
    private final GuiScreen parentScreen;

    private GuiButton buttonCopyLink;
    private String message;
    private String linkUrl;
    private long messageHideTimeMs;

    public GuiScreenCapeOF(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    @Override
    public void initGui() {
        int y = 0;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height / 6 + 24 * ((y += 2) >> 1), 150, 20,
                "Open Cape Editor"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + 160, this.height / 6 + 24 * (y >> 1), 150, 20,
                "Reload Cape"));
        this.buttonCopyLink = new GuiButton(2, this.width / 2 - 100, this.height / 6 + 24 * ((y += 6) >> 1), 200, 20,
                "Copy Editor Link");
        this.buttonCopyLink.visible = this.linkUrl != null;
        this.buttonList.add(this.buttonCopyLink);
        this.buttonList.add(new GuiButton(99, this.width / 2 - 100, this.height / 6 + 24 * ((y += 4) >> 1),
                I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }

        switch (button.id) {
            case 99:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            case 0:
                BigInteger firstRandom = new BigInteger(128, new Random());
                BigInteger secondRandom = new BigInteger(128, new Random(System.identityHashCode(new Object())));
                BigInteger serverRandom = firstRandom.xor(secondRandom);
                String serverId = serverRandom.toString(16);

                try {
                    this.mc.getSessionService().joinServer(this.mc.getSession().getProfile(),
                            this.mc.getSession().getToken(), serverId);
                    String capeChangeUri = "https://optifine.net/capeChange?u="
                            + this.mc.getSession().getProfile().getId().toString().replace("-", "")
                            + "&n=" + this.mc.getSession().getProfile().getName() + "&s=" + serverId;
                    if (this.openWebLink(URI.create(capeChangeUri))) {
                        this.showMessage("The OptiFine cape editor should open in a web browser.", 10000L);
                    } else {
                        this.showMessage("Error opening OptiFine cape editor.", 10000L);
                        this.setLinkUrl(capeChangeUri);
                        this.buttonCopyLink.visible = true;
                    }
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                this.showMessage("The cape will be reloaded in 15 seconds.", 15000L);

                if (this.mc.thePlayer != null) {
                    this.mc.thePlayer.setReloadCapeTimeMs(System.currentTimeMillis() + 15000L);
                }
                break;
            case 2:
                if (this.linkUrl != null) {
                    GuiScreen.setClipboardString(this.linkUrl);
                }
                break;
            default:
                break;
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
            this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.height / 6 + 60, 0xFFFFFFFF);

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
 