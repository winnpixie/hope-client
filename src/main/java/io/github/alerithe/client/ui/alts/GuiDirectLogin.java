package io.github.alerithe.client.ui.alts;

import io.github.alerithe.client.utilities.SessionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class GuiDirectLogin extends GuiScreen {
    private GuiButton modeButton;
    private GuiTextField username;
    private GuiPasswordField password;
    private String message;
    private int mode;

    @Override
    public void initGui() {
        message = String.format("\247eCurrent User : \247r%s", mc.getSession().getUsername());
        if (mc.getSession().getToken().isEmpty()) {
            message = "[Cracked] " + message;
        }
        username = new GuiTextField(0, fontRenderer, width / 2 - 100, height / 2 - 30, 200, 20);
        username.setMaxStringLength(32767);
        username.setFocused(true);
        password = new GuiPasswordField(1, fontRenderer, width / 2 - 100, height / 2 + 10, 200, 20);
        password.setMaxStringLength(32767);
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 35, 98, 20, "Log In"));
        buttonList.add(modeButton = new GuiButton(1, width / 2 + 2, height / 2 + 35, 98, 20, "Microsoft OAuth"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 56, 98, 20, "Set Cracked"));
        buttonList.add(new GuiButton(3, width / 2 + 2, height / 2 + 56, 98, 20, "Import Clipboard"));
        buttonList.add(new GuiButton(4, width / 2 - 100, height / 2 + 77, "Exit"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                logIn();
                break;
            case 1:
                mode = (mode + 1) % 3;

                switch (mode) {
                    case 0:
                        modeButton.displayString = "Microsoft OAuth";
                        break;
                    case 1:
                        modeButton.displayString = "Microsoft";
                        break;
                    case 2:
                        modeButton.displayString = "Mojang";
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                if (username.getText().isEmpty()) {
                    message = "\247cMissing username.";
                    break;
                }

                String user = username.getText();
                if (user.length() > 16) user = user.substring(0, 16);

                mc.setSession(new Session(user, UUID.randomUUID().toString(), "", "legacy"));
                message = String.format("[Cracked] \247eCurrent User : \247r%s", mc.getSession().getUsername());
                break;
            case 3:
                try {
                    String text = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    String[] data = text.split(":", 2);

                    username.setText(data[0]);
                    if (data.length > 1) {
                        password.setText(data[1]);
                    }
                } catch (UnsupportedFlavorException e) {
                    message = String.format("\247e%s", e.getMessage());
                }
                break;
            case 4:
                mc.displayGuiScreen(new GuiMainMenu());
                break;
            default:
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_TAB) {
            username.setFocused(!username.isFocused());
            password.setFocused(!password.isFocused());
        } else if (keyCode == Keyboard.KEY_RETURN) {
            logIn();
        }
        username.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, message, width / 2, 10, -1);

        fontRenderer.drawStringWithShadow("Username", this.width / 2f - 100f, this.height / 2f - 40f, -1);
        username.drawTextBox();

        fontRenderer.drawStringWithShadow("Password", this.width / 2f - 100f, this.height / 2f, -1);
        password.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void logIn() {
        if (mode > 0
                && (username.getText().isEmpty() || password.getText().isEmpty())) {
            message = "\247cInsufficient information provided.";
            return;
        }

        message = "\247aLogging in...";
        new Thread(() -> {
            try {
                Session session;

                switch (mode) {
                    case 0: // Microsoft OAuth
                        session = SessionHelper.logInWithMicrosoft(deviceCode -> {
                            message = String.format("\247eVISIT: \247f%s", deviceCode.getDirectVerificationUri());

                            try {
                                Desktop.getDesktop().browse(
                                        URI.create(deviceCode.getDirectVerificationUri()));
                            } catch (IOException ioe) {
                                message = String.format("\247e%s", ioe.getMessage());
                            }
                        });
                        break;
                    case 1: // Microsoft Credentials
                        session = SessionHelper.logInWithMicrosoft(username.getText(), password.getText());
                        break;
                    case 2: // Mojang Credentials
                        session = SessionHelper.logInWithMojang(username.getText(), password.getText());
                        break;
                    default:
                        session = null;
                        break;
                }

                if (session == null) {
                    throw new IllegalStateException("session should not be null!");
                }

                mc.setSession(session);
                message = String.format("\247eCurrent User : \247r%s", mc.getSession().getUsername());
            } catch (Exception e) {
                message = String.format("\247c%s", e.getMessage());
            }
        }).start();
    }
}
