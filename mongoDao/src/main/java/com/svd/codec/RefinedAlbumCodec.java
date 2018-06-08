package com.svd.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import refinedDataModels.RefinedAlbum;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RefinedAlbumCodec implements Codec<RefinedAlbum> {

    /**
     *
     * {
     *      "a" : "value",
     *      "b": [
     *          1,
     *          2
     *      ]
     * }
     *
     *
     * @param reader
     * @param decoderContext
     * @return
     */
    @Override
    public RefinedAlbum decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String a = reader.readString();
        reader.readStartArray();
        int[] b = new int[]{reader.readInt32(), reader.readInt32()};
        reader.readEndArray();
        reader.readEndDocument();
        try {
            RefinedAlbum newRefinedAlbum = RefinedAlbum.builder()
                    .id(reader.readString("id"))
                    .name(reader.readString("name"))
                    .imageURL(new URL(reader.readString("imageURL")))
                    .popularity(reader.readInt32("popularity"))
                    .externalURL(new URL(reader.readString("externalURL")))
                    .href(new URL(reader.readString("href")))
                    .spotifyURI(reader.readString("spotifyURI"))
                    .build();
            reader.readEndDocument();
            return newRefinedAlbum;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
return null;
    }

    @Override
    public void encode(BsonWriter writer, RefinedAlbum refinedAlbum, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("id", refinedAlbum.getId());
        writer.writeString("name", refinedAlbum.getName());

        writer.writeString("imageURL", refinedAlbum.getImageURL().toString());
        writer.writeString("releaseDate", refinedAlbum.getReleaseDate().toString());
        writer.writeInt32("popularity", refinedAlbum.getPopularity());
        writer.writeString("externalURL", refinedAlbum.getExternalURL().toString());
        writer.writeString("href", refinedAlbum.getHref().toString());
        writer.writeString("spotifyURI", refinedAlbum.getSpotifyURI());
        writer.writeEndDocument();

    }

    @Override
    public Class<RefinedAlbum> getEncoderClass() {
        return RefinedAlbum.class;
    }
}
