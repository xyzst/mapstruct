/**
 *  Copyright 2012-2015 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.model.source.selector;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.ap.model.common.Type;
import org.mapstruct.ap.model.source.Method;

/**
 * Selects on inheritance distance, e.g. the amount of inheritance steps from the target type.
 *
 * @author Sjaak Derksen
 */
public class InheritanceTargetSelector implements MethodSelector {

    @Override
    public <T extends Method> List<T> getMatchingMethods(
        Method mappingMethod,
        List<T> methods,
        Type sourceType,
        Type targetType,
        SelectionCriteria criteria
    ) {

        List<T> candidatesWithBestMatchingTargetType = new ArrayList<T>();


        // hierarchy fork detection
        boolean inheritanceForkDetected = false;
        for ( int i = 0; i < methods.size(); i++ ) {

            if ( i < ( methods.size() - 1 ) ) {
                for ( int j = i + 1; j < methods.size(); j++ ) {
                    if ( !(methods.get( i ).getResultType().isAssignableTo( methods.get( j ).getResultType() )
                        || methods.get( j ).getResultType().isAssignableTo( methods.get( i ).getResultType() ) ) ) {
                        // we are NOT dealing with one continues type hierarchy
                        // Suppose: Sedan -> Car -> Vehicle, MotorCycle -> Vehicle
                        //          When matching targettype Vehicle, Sedan ain't a better resulttype candidate than
                        //          MotorCycle
                        inheritanceForkDetected = true;
                        break;
                    }
                }
            }
        }

        // distance detection
        int bestMatchingtargetTypeDistance = Integer.MAX_VALUE;
        if ( candidatesWithBestMatchingTargetType.isEmpty() ) {
            // find the methods with the minimum distance regarding getParameter getParameter type
            for ( T method : methods ) {

                int targetTypeDistance = method.getResultType().distanceTo( targetType );
                bestMatchingtargetTypeDistance
                    = addToCandidateListIfMinimal(
                        candidatesWithBestMatchingTargetType,
                        bestMatchingtargetTypeDistance,
                        method,
                        targetTypeDistance
                    );
            }
        }

        if ( !inheritanceForkDetected || ( bestMatchingtargetTypeDistance == 0 ) ) {
            return candidatesWithBestMatchingTargetType;
        }
        else {
            return methods;
        }
    }

    private <T extends Method> int addToCandidateListIfMinimal(List<T> candidatesWithBestMathingType,
                                                               int bestMatchingTypeDistance, T method,
                                                               int currentTypeDistance) {
        if ( currentTypeDistance == bestMatchingTypeDistance ) {
            candidatesWithBestMathingType.add( method );
        }
        else if ( currentTypeDistance < bestMatchingTypeDistance ) {
            bestMatchingTypeDistance = currentTypeDistance;

            candidatesWithBestMathingType.clear();
            candidatesWithBestMathingType.add( method );
        }
        return bestMatchingTypeDistance;
    }

}
