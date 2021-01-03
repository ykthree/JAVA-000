function decrease_inventory()
    if (redis.call(get(inventory)) == 0) then
        return 0;
    else
        return redis.call(decr(inventory));
    end
end
