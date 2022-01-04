package de.bentzin.bot.command.vp;

import de.bentzin.facharbeit.bot.Bot;
import de.bentzin.facharbeit.bot.PDFTest;
import lombok.SneakyThrows;

import java.io.File;
import java.util.TimerTask;


/**
 * The type Vp task.
 */
public final class VPTask extends TimerTask {

    private static VPTask vpTask = new VPTask();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static VPTask getInstance() {
        return vpTask;
    }
    /**
     * The action to be performed by this timer task.
     */
    @SneakyThrows
    @Override
    public void run() {
        PDFTest.sendPDF(Bot.getChat(), new File("D:/MainDesktop/WABot/KeineDaten.pdf"));
    }
}
