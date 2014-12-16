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
package org.mapstruct.ap.test.selection.inheritance;

import javax.tools.Diagnostic.Kind;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.compilation.annotation.CompilationResult;
import org.mapstruct.ap.testutil.compilation.annotation.Diagnostic;
import org.mapstruct.ap.testutil.compilation.annotation.ExpectedCompilationOutcome;
import org.mapstruct.ap.testutil.runner.AnnotationProcessorTestRunner;

/**
 *
 * @author Sjaak Derksen
 */
@IssueKey("385")
@WithClasses({
    Fruit.class,
    FruitDto.class,
    Apple.class,
    AppleDto.class,
    GoldenDelicious.class,
    GoldenDeliciousDto.class,
    FruitFactory.class,
    FruitMapper.class
})

@RunWith(AnnotationProcessorTestRunner.class)
public class InheritanceSelectionTest {


    @Test
    public void testInheritanceBestFitSource() {

        Fruit fruit = new Fruit( null );
        FruitDto fruitDto = FruitMapper.INSTANCE.map( fruit );
        assertThat( fruitDto ).isNotNull();
        assertThat( fruitDto.getType() ).isEqualTo( "fruit" );

        Apple apple = new Apple( null );
        AppleDto appleDto = FruitMapper.INSTANCE.map( apple );
        assertThat( appleDto ).isNotNull();
        assertThat( appleDto.getType() ).isEqualTo( "apple" );

        GoldenDelicious goldenDelicious = new GoldenDelicious( null );
        GoldenDeliciousDto goldenDeliciousDto = FruitMapper.INSTANCE.map( goldenDelicious );
        assertThat( goldenDeliciousDto ).isNotNull();
        assertThat( goldenDeliciousDto.getType() ).isEqualTo( "goldenDelicious" );
    }

    @Test
    public void testInheritanceBestFitTarget() {

        FruitDto fruitDto = new FruitDto( null );
        Fruit fruit = FruitMapper.INSTANCE.map( fruitDto );
        assertThat( fruit ).isNotNull();
        assertThat( fruit.getType() ).isEqualTo( "fruit" );

        AppleDto appleDto = new AppleDto( null );
        Apple apple = FruitMapper.INSTANCE.map( appleDto );
        assertThat( apple ).isNotNull();
        assertThat( apple.getType() ).isEqualTo( "apple" );

        GoldenDeliciousDto goldenDeliciousDto = new GoldenDeliciousDto( null );
        GoldenDelicious goldenDelicious = FruitMapper.INSTANCE.map( goldenDeliciousDto );
        assertThat( goldenDelicious ).isNotNull();
        assertThat( goldenDelicious.getType() ).isEqualTo( "goldenDelicious" );

    }

    @Test
    @WithClasses( { ExactMatchFruitFactory.class, ExactMatchFruitMapper.class, Banana.class } )
    public void testForkedInheritanceHierarchyButWithDistanceZeroShouldResultInSelectionOfExactMatch() {

        FruitDto fruitDto = new FruitDto( null );
        Fruit fruit = FruitMapper.INSTANCE.map( fruitDto );
        assertThat( fruit ).isNotNull();
        assertThat( fruit.getType() ).isEqualTo( "fruit" );

    }

    @Test
    @WithClasses( { ConflictingFruitFactory.class, ErroneousFruitMapper.class, Banana.class } )
    @ExpectedCompilationOutcome(
        value = CompilationResult.FAILED,
        diagnostics = {
            @Diagnostic(type = ErroneousFruitMapper.class,
                kind = Kind.ERROR,
                line = 36,
                messageRegExp = "Ambiguous mapping methods found for factorizing .*Fruit: "
                    + ".*Apple .*ConflictingFruitFactory\\.createApple\\(\\), "
                    + ".*Banana .*ConflictingFruitFactory\\.createBanana\\(\\)\\.")
        }
    )
    public void testForkedInheritanceHierarchyShouldResultInAmbigousMappingMethod() {
    }

    @Test
    @WithClasses( { ConflictingFruitFactory.class, TargetTypeSelectingFruitMapper.class, Banana.class } )
    public void testForkedInheritanceHierarchyButDefinedTargetType() {

        FruitDto fruitDto = new FruitDto( null );
        Fruit fruit = TargetTypeSelectingFruitMapper.INSTANCE.map( fruitDto );
        assertThat( fruit ).isNotNull();
        assertThat( fruit.getType() ).isEqualTo( "apple" );

    }

}
