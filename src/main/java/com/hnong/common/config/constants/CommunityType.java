package com.hnong.common.config.constants;


/**
 * Community type defines all the community supported types
 * E.g novel, news, shopping and images
 */
public enum CommunityType {
    /**
     * Novel community type
     */
    NOVEL(1, "novel"),

    /**
     * News community type
     */
    NEWS(2, "news"),

    /**
     * Shopping community type
     */
    SHOPPING(3, "shopping"),

    /**
     * Image community type
     */
    IMAGE(4, "image");

    private Integer id;

    private String name;

    private CommunityType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get defined id
     * @return Pre defined id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get defined name
     * @return Pre defined name
     */
    public String getName() {
        return name;
    }

    /**
     * Get defined communityType
     * @param id
     * @return Pre defined communityType
     */
    public static CommunityType getCommunityTypeById(Integer id) {
        CommunityType communityType = null;
        for (CommunityType type : CommunityType.values()) {
            if (type.getId().equals(id)) {
                communityType = type;
                break;
            }
        }
        return communityType;
    }

    /**
     * Get defined communityType
     * @param name
     * @return Pre defined communityType
     */
    public static CommunityType getCommunityTypeByName(String name) {
        CommunityType communityType = null;
        for (CommunityType type : CommunityType.values()) {
            if (type.getId().equals(name.toLowerCase())) {
                communityType = type;
                break;
            }
        }
        return communityType;
    }
}
