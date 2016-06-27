# Persistence layer for Scala training sample project

See [specification project](https://github.com/SeanShubin/todo-specification/blob/master/README.md) for documentation

### To do the training exercise

- checkout the branch
    -     git checkout training
- rebuild the executable
    -     mvn package
- implement the ConfigurationWiring
    - Negative demo
        - prove that the web service is not working in some way, possibly because of an empty implementation
    - Test
        - write a test for the component that is not working
    - Implement
        - get a single component working, here you will notice the collaborators you need
    - Collaborator Contract
        - create the contracts for the collaborators, and stub these out in the test
    - Collaborator Empty Implementation
        - create empty implementations of the collaborators
    - Inject
        - update the wiring to constructor inject empty implementations of collaborators
    - Positive demo
        - prove that the web service gets further than it did before, and discover which collaborator is needed next
    - Repeat
        - The positive demo for the previous component becomes the negative demo for the next component, so repeat the process until everything works
- Demo starting points (get working in this order)
    - [localhost:7002/health](localhost:7002/health)
    - [localhost:7002/task](localhost:7002/task)
    - [localhost:7001](localhost:7001)