package knowledgeswap2.storage;

import knowledgeswap2.service.FeedbackSystem;

import java.io.*;

public class DataStorage {
    private final File dataFolder;
    private final File dataFile;

    public DataStorage() {
        dataFolder = new File(System.getProperty("user.dir"), "data");
        dataFile = new File(dataFolder, "knowledge_swap2.ser");
    }

    public File getDataFile() {
        return dataFile;
    }

    public void save(FeedbackSystem system) throws IOException {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IOException("Could not create data folder: " + dataFolder.getAbsolutePath());
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(system);
        }
    }

    public FeedbackSystem load() throws IOException, ClassNotFoundException {
        if (!dataFile.exists()) return new FeedbackSystem();
        if (dataFile.length() < 16) {
            throw new EOFException("Saved data file is incomplete: " + dataFile.getAbsolutePath());
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (FeedbackSystem) in.readObject();
        }
    }

    public void backupBadDataFile(Exception ex) {
        if (!dataFile.exists()) return;
        String suffix = ".bad-" + System.currentTimeMillis();
        File backup = new File(dataFile.getParentFile(), dataFile.getName() + suffix);
        try {
            boolean ok = dataFile.renameTo(backup);
            if (!ok) {
                try (InputStream in = new FileInputStream(dataFile);
                     OutputStream out = new FileOutputStream(backup)) {
                    in.transferTo(out);
                }
                // best effort delete
                dataFile.delete();
            }
        } catch (Exception ignored) {
            // best effort only
        }
    }
}

