package com.betting.ground.auction.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ViewCacheRepository {

    private final RedisTemplate redisTemplate;
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

    public List<String> getAllAuctions(){
        return setAuction.members(AUCTION).stream().toList();
    }

    public void removeAllAuctions(){
        setAuction.remove(AUCTION, this.getAllAuctions().toArray());
    }

    public void setUUID(String auctionId, String uuid) {
        setAuction.add(getUniqueKey(auctionId), uuid.toString());
    }

    public void removeAllUUID(){
        List<String> allAuctions = getAllAuctions();
        for (String auctionId : allAuctions) {
            List<String> members = getMembers(auctionId).stream().toList();
            for (String uuid : members) {
                setAuction.remove(getUniqueKey(auctionId), uuid);
            }
        }
    }

    public int getViewCount(String auctionId) {
        return setAuction.members(getUniqueKey(auctionId)).size();
    }

    public boolean addUniqueUUID(String auctionId, String uuid){
        return valueOperations.setIfAbsent(
                getUniqueUUIDKey(auctionId),
                uuid,
                Duration.ofMinutes(5)
        );
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
