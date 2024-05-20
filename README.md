# NewLang
This repository contains the five projects developed during the *Compilers* university course. The repository here is a mirror of the GitLab private one where the exercises were delivered for scoring purposes. The first four exercises have a simplified set of requirements (lexical and syntactic specifications), to gather enough experience for the last two exercises (exercise 4 and 5) where NewLang is actually implemented. Each exercise is manually tested with source code test files provided as input to ensure the correctness of the behavior both on correct source code files and on wrong ones. The fifth exercise has a simple GitLab CI step to automatically verify correct execution on a subset of the tests files, provided by the professor. The book **Compilers: Principles, Techniques, and Tools (2nd Edition)** was used during the whole course. Part of the exam was done by implementing support for a new feature, requiring an edit in all the modules. This implementation is not present in this repository as for now.

The **1_hand_coded_lexer** folder contains the first exercise, consisting in the development of a hand-made lexer for a simple set of tokens (delimiters, keywords, IDs, numerical literals, separators and basic operators).

The **2_jflex_lexer** folder is a remake of the first exercise, using JFlex.

The **3_hand_coded_parser** folder uses the hand-made lexer and a simple grammar for the implementation of a top-down recursive descent parser. The grammar provided was tweaked to support the implementation for such a parser, without altering its resulting language.

The **4_CUP_parser** folder implements the lexical and syntactic analysis steps with the use of JFlex and CUP. Once again, the provided grammar was slightly tweaked to support the implementation of the CUP parser. Several tests are provided to confirm the correct behavior of the resulting implementations. A XML visitor is provided to allow visualizing the resulting syntax tree generated from the implementation on a specific program passed via input.

The **5_semantic_analysis_C_gen** expands the work done in the previous exercise by using the generated syntax tree to perform semantic analysis and C code generation. The semantic analysis implements scope handling, type inference and type checking based on IF-THEN inference rules. The type system specification 

Examples of NewLang source code files can be seen in the fifth exercise folder, within the **test_files** and **tests** folders.