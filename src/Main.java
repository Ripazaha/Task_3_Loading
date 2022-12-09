import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void openZip(String thePathToTheFileZip, String pathToFolderToUnzip) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(thePathToTheFileZip))) {
            ZipEntry entry;
            String nameFile;
            while ((entry = zis.getNextEntry()) != null) {
                nameFile = entry.getName();
                FileOutputStream fos = new FileOutputStream(pathToFolderToUnzip + nameFile);
                for (int fileSave = zis.read(); fileSave != -1; fileSave = zis.read()) {
                    fos.write(fileSave);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String saveFilePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(saveFilePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

    public static void main(String[] args) {
        String thePathToTheFileZip = null;
        String pathToFolderToUnzip = "C:\\User\\Games\\savegames\\";
        String saveFilePath = null;

        // считываем архив
        File dir = new File(pathToFolderToUnzip);
        for (File item : Objects.requireNonNull(dir.listFiles())) {
            if (dir.isDirectory()) {
                thePathToTheFileZip = pathToFolderToUnzip + item.getName();
                openZip(thePathToTheFileZip, pathToFolderToUnzip);
                saveFilePath = item.getName();
            }
        }


        // десериализация
        GameProgress savedGame;
        for (File item : Objects.requireNonNull(dir.listFiles())) {
            if (dir.isDirectory()) {
                saveFilePath = item.getName();
            }
        }
        savedGame = openProgress(pathToFolderToUnzip + saveFilePath);
        System.out.println(savedGame);
    }
}
