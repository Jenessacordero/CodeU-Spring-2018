package codeu.model.store.basic;

import codeu.model.data.Images;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class UploadedImagesStoreTest {

    private UploadedImagesStore imagesStore;
    private PersistentStorageAgent mockPersistentStorageAgent;

    private final Images image_1 =
            new Images(
                    "file1",
                    "random",
                    UUID.randomUUID());
    private final Images image_2 =
            new Images(
                    "file2",
                    "random",
                    UUID.randomUUID());
    private final Images image_3 =
            new Images(
                    "file3",
                    "random",
                    UUID.randomUUID());

    @Before
    public void setup() {
        mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
        imagesStore = UploadedImagesStore.getTestInstance(mockPersistentStorageAgent);

        final List<Images> imageList = new ArrayList<>();
        imageList.add(image_1);
        imageList.add(image_2);
        imageList.add(image_3);
        imagesStore.setImageStore(imageList);
    }

    @Test
    public void testReturnAllImages() {
        List<Images> allImages = imagesStore.returnAllImages();

        Assert.assertEquals(3, allImages.size());
        assertEquals(image_1, allImages.get(0));
        assertEquals(image_2, allImages.get(1));
        assertEquals(image_3, allImages.get(2));
    }

    @Test
    public void testAddImage() {
        Images imageUpdate =
                new Images(
                        "file4", "random", UUID.randomUUID());

        imagesStore.addImage(imageUpdate);
        Images imageUpdateResult = imagesStore.returnAllImages().get(3);

        assertEquals(imageUpdate, imageUpdateResult);
        Mockito.verify(mockPersistentStorageAgent).writeThrough(imageUpdate);
    }

    private void assertEquals(Images expectedImage, Images actualImage) {
        Assert.assertEquals(expectedImage.returnDestination(), actualImage.returnDestination());
        Assert.assertEquals(expectedImage.returnFileName(), actualImage.returnFileName());
        Assert.assertEquals(expectedImage.getID(), actualImage.getID());
    }
}
