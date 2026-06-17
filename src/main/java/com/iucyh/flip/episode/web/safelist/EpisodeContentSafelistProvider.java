package com.iucyh.flip.episode.web.safelist;

import com.iucyh.flip.core.json.deserializer.html.registry.SafelistProvider;
import com.iucyh.flip.episode.constant.EpisodeConstants;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class EpisodeContentSafelistProvider implements SafelistProvider {

    @Override
    public String getKey() {
        return EpisodeConstants.EPISODE_CONTENT_SAFE_LIST_KEY;
    }

    @Override
    public Safelist getSafelistPolicy() {
        return new Safelist()
                .addTags("p", "b", "strong", "i", "em", "u", "br", "span");
    }
}
