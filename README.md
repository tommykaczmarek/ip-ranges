# Ip ranges  
### Utilities for processing set of ip addresses ranges finding and removing their intersections.
Returns set of mutually exclusive ip addresses - symmetric difference

### Requirements:
- ip range must be proper IPv4 format
  ```
  192.160.1.100
  ```
- ip range must be contiguous 
  ```
  197.203.0.0 - 197.203.255.255
  ```
  not
  ```
  192.168.0.0 - 10.1.233.101  
  ``` 

#### Ip ranges library
Takes ip ranges as input, perform validation and returns symmetric difference
Example:
For following ip ranges:
```
    IpRange2("197.203.0.0", "197.206.9.255"),
    IpRange2("197.204.0.0", "197.204.0.24"),
    IpRange2("201.233.7.160", "201.233.7.168"),
    IpRange2("201.233.7.164", "201.233.7.168"),
    IpRange2("201.233.7.167", "201.233.7.167"),
    IpRange2("203.133.0.0", "203.133.255.255")
```

it returns 
```
    IpRange2("197.203.0.0", "197.203.255.255"),
    IpRange2("197.204.0.25", "197.206.9.255"),
    IpRange2("201.233.7.160", "201.233.7.163"),
    IpRange2("203.133.0.0", "203.133.255.255")
```
# See exmple library usage at
com.losmotylos.ip.ranges.IpRangesTest
    

#### Ip ranges spark application 
# Takes ip ranges with associated URI, returns symmetric difference with URI

   Input
   ```
   DataSet[IpRangesMessage] 
   where
   IpRangesMessage(
        "BU1LP6a7vCGiqb", 
        Seq(
            IpRange2("197.203.0.0", "197.206.9.255"),
            IpRange2("197.204.0.0", "197.204.0.24")
        )
   )
   ``` 
   Returns one of:
   - IpRangesMessage 
   When operation succeeds - Uri with Seq of IpRanges containing symmetric difference 
   - IpRange2ValidationError
   When error occurs due - error with input ranges

# See example usage in spark at
com.losmotylos.ip.ranges.IpRangesIntegrationTest


   
    
      
   