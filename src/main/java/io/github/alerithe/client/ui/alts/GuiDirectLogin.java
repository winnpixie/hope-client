package io.github.alerithe.client.ui.alts;

import com.mojang.authlib.exceptions.AuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import io.github.alerithe.client.utilities.Wrapper;
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
import java.util.UUID;

public class GuiDirectLogin extends GuiScreen {
    private GuiTextField username;
    private GuiPasswordField password;
    private String message;

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
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 35, "Log In"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 + 55, "Import USER:PASS"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 75, "Exit"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                logIn();
                break;
            case 1:
                try {
                    String text = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    String[] data = text.split(":", 2);
                    username.setText(data[0]);
                    if (data.length > 1) {
                        password.setText(data[1]);
                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                mc.displayGuiScreen(new GuiMainMenu());
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

        fontRenderer.drawStringWithShadow("Username", this.width / 2 - 100, this.height / 2 - 40, -1);
        username.drawTextBox();

        fontRenderer.drawStringWithShadow("Password", this.width / 2 - 100, this.height / 2, -1);
        password.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void logIn() {
        if (!username.getText().isEmpty()) {
            if (!password.getText().isEmpty()) {
                message = "\247aLogging in...";
                new Thread(() -> {
                    try {
                        if (isShiftKeyDown()) {
                            mc.setSession(Wrapper.createMojangSession(username.getText(), password.getText()));
                        } else {
                            mc.setSession(Wrapper.createMicrosoftSession(username.getText(), password.getText()));
                        }

                        message = String.format("\247eCurrent User : \247r%s", mc.getSession().getUsername());
                    } catch (AuthenticationException | MicrosoftAuthenticationException e) {
                        e.printStackTrace();
                        message = String.format("\247c%s", e.getMessage());
                    }
                }).start();
            } else {
                mc.setSession(new Session(username.getText(), UUID.randomUUID().toString(), "", "legacy"));
                message = String.format("[Cracked] \247eCurrent User : \247r%s", mc.getSession().getUsername());
            }
        } else {
            message = "\247cInsufficient information provided.";
        }
    }
}
