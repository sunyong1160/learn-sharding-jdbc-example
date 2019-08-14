package learn.sharding.jdbc.example.leetcode.easy._001;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solution {

    public static void main(String[] args){

        Solution solution = new Solution();
        int[] nums = new int[]{2,7,11,13};
        int target = 9;
        System.out.println(Arrays.toString(solution.twoSum(nums, target)));

    }

    private int[] twoSum(int[] nums, int target) {

        int length = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=0;i<length;i++){
            Integer value = map.get(nums[i]);
            if(value != null){
                return new int[]{value, i};
            }
            map.put(target - nums[i], i);
        }
        return null;
    }
}
