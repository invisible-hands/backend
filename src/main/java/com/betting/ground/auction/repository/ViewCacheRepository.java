package com.betting.ground.auction.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ViewCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private SetOperations<String, String> setAuction;
    private ValueOperations<String, String> valueOperations;
    private static final String AUCTION = "Auction";
    private static final String UNIQUE_AUCTION_KEY = "UniqueAuctionKey::";
    private static final String UUID = "Auction::";


    @PostConstruct
    public void setUp() {
        setAuction = redisTemplate.opsForSet();
        valueOperations = redisTemplate.opsForValue();
    }

    public void setAuction(String auctionId){
        setAuction.add(AUCTION, auctionId);
    }

    public Set<String> getAllAuctions(){
        return setAuction.members(AUCTION);
    }

    public void removeAllAuctions(){
        setAuction.remove(AUCTION, this.getAllAuctions().toArray());
    }

    public void setUUID(String auctionId, String uuid) {
        setAuction.add(getUniqueKey(auctionId), uuid);
    }

    public void removeAllUUID(){
        Set<String> allAuctions = getAllAuctions();
        for (String auctionId : allAuctions) {
            Set<String> members = getMembers(auctionId);
            for (String uuid : members) {
                setAuction.remove(getUniqueKey(auctionId), uuid);
            }
        }
    }

    public int getViewCount(String auctionId) {
        return setAuction.members(getUniqueKey(auctionId)).size();
    }

    public boolean addUniqueUUID(String auctionId, String uuid){
        return Boolean.TRUE.equals(valueOperations.setIfAbsent(
                getUniqueUUIDKey(auctionId),
                uuid,
                Duration.ofMinutes(5)
        ));
    }

    private String getUniqueKey(String auctionId){
        return UNIQUE_AUCTION_KEY + auctionId;
    }

    private String getUniqueUUIDKey(String auctionId){
        return UUID + auctionId;
    }

    private Set<String> getMembers(String auctionId) {
        return setAuction.members(getUniqueKey(auctionId));
    }
}
