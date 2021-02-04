package com.example.site.model.util;

import com.example.site.model.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author){
        return author != null ? author.getUsername():"<none>";
    }
}
